COMMITS=$(git log --oneline|cut -c 1-7|tac)
rm -rf res
mkdir -p res
for commit in $COMMITS
 do
 git checkout $commit
 mvn clean test -f good-pom.xml
 EPOCH_LAST_COMMIT=$(git log -1 --format=%ct)
 cp ./target/site/jacoco-ut/jacoco.xml  res/${EPOCH_LAST_COMMIT}-$commit-jacoco.xml
done
